import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getPendingApprovals, approveInvoice, rejectInvoice } from "../../api/approvalApi";

function ApprovalList() {

    const [invoices, setInvoices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [busyId, setBusyId] = useState(null);
    const [remarksById, setRemarksById] = useState({});

    const load = async () => {

        try {
            const response = await getPendingApprovals();
            setInvoices(response.data);
        } catch (error) {
            toast.error("Failed to load pending approvals");
        } finally {
            setLoading(false);
        }

    };

    useEffect(() => { load(); }, []);

    const setRemarks = (id, value) => {
        setRemarksById((prev) => ({ ...prev, [id]: value }));
    };

    const handleDecision = async (id, approve) => {

        const remarks = (remarksById[id] || "").trim();

        if (!remarks) {
            toast.error("Please enter remarks before approving or rejecting.");
            return;
        }

        setBusyId(id);

        try {

            if (approve) {
                await approveInvoice(id, remarks);
                toast.success(`Invoice #${id} approved`);
            } else {
                await rejectInvoice(id, remarks);
                toast.success(`Invoice #${id} rejected`);
            }

            setInvoices((prev) => prev.filter((inv) => inv.id !== id));

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Action failed"
            );

        } finally {

            setBusyId(null);

        }

    };

    return (
        <DashboardLayout>

            <h2 className="mb-4">Pending Approvals</h2>

            <p className="text-muted">
                Invoices that passed three-way matching and are awaiting an approve/reject decision.
            </p>

            {loading ? (
                <div className="text-center">Loading...</div>
            ) : invoices.length === 0 ? (
                <div className="alert alert-success">Nothing pending approval 🎉</div>
            ) : (
                invoices.map((inv) => (
                    <div className="card mb-3" key={inv.id}>
                        <div className="card-body">
                            <div className="d-flex justify-content-between align-items-start mb-3">
                                <div>
                                    <h5 className="mb-1">
                                        Invoice {inv.invoiceNumber} — {inv.vendorName}
                                    </h5>
                                    <div className="text-muted small">
                                        PO {inv.poNumber} · Total ₹ {Number(inv.totalAmount || 0).toFixed(2)}
                                    </div>
                                </div>
                                <Link to={`/invoices/${inv.id}`} className="btn btn-outline-secondary btn-sm">
                                    View Details
                                </Link>
                            </div>

                            <div className="row g-2 align-items-center">
                                <div className="col-md-8">
                                    <input
                                        type="text"
                                        className="form-control"
                                        placeholder="Remarks (required)"
                                        value={remarksById[inv.id] || ""}
                                        onChange={(e) => setRemarks(inv.id, e.target.value)}
                                    />
                                </div>
                                <div className="col-md-4 d-flex gap-2">
                                    <button
                                        className="btn btn-success flex-fill"
                                        disabled={busyId === inv.id}
                                        onClick={() => handleDecision(inv.id, true)}
                                    >
                                        Approve
                                    </button>
                                    <button
                                        className="btn btn-danger flex-fill"
                                        disabled={busyId === inv.id}
                                        onClick={() => handleDecision(inv.id, false)}
                                    >
                                        Reject
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                ))
            )}

        </DashboardLayout>
    );
}

export default ApprovalList;
