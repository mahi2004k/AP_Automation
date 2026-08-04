import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getInvoices } from "../../api/invoiceApi";

const STATUS_BADGE = {
    UPLOADED: "secondary",
    PROCESSING: "secondary",
    EXTRACTED: "info",
    NEEDS_REVIEW: "warning",
    MATCHED: "primary",
    APPROVED: "success",
    PENDING: "secondary",
    REJECTED: "danger",
    PAID: "success"
};

function InvoiceList() {

    const [invoices, setInvoices] = useState([]);
    const [statusFilter, setStatusFilter] = useState("");
    const [loading, setLoading] = useState(true);

    const load = async (status) => {

        setLoading(true);

        try {

            const response = await getInvoices(status || undefined);
            setInvoices(response.data);

        } catch (error) {

            toast.error("Failed to load invoices");

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {
        load(statusFilter);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [statusFilter]);

    return (
        <DashboardLayout>

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2>Invoices</h2>

                <Link to="/invoices/upload" className="btn btn-primary">
                    + Upload Invoice
                </Link>

            </div>

            <div className="mb-3" style={{ maxWidth: 260 }}>

                <select
                    className="form-select"
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                >
                    <option value="">All Statuses</option>
                    <option value="UPLOADED">Uploaded</option>
                    <option value="EXTRACTED">Extracted</option>
                    <option value="NEEDS_REVIEW">Needs Review</option>
                    <option value="MATCHED">Matched</option>
                    <option value="APPROVED">Approved</option>
                    <option value="REJECTED">Rejected</option>
                    <option value="PAID">Paid</option>
                </select>

            </div>

            {loading ? (

                <div className="text-center">Loading...</div>

            ) : (

                <table className="table table-bordered table-hover">

                    <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Invoice #</th>
                            <th>Vendor</th>
                            <th>PO #</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>

                        {invoices.length === 0 ? (
                            <tr>
                                <td colSpan="7" className="text-center">
                                    No invoices found
                                </td>
                            </tr>
                        ) : (
                            invoices.map((inv) => (
                                <tr key={inv.id}>
                                    <td>{inv.id}</td>
                                    <td>{inv.invoiceNumber || "—"}</td>
                                    <td>{inv.vendorName || "—"}</td>
                                    <td>{inv.poNumber || "—"}</td>
                                    <td>{inv.totalAmount != null ? `₹ ${Number(inv.totalAmount).toFixed(2)}` : "—"}</td>
                                    <td>
                                        <span className={`badge bg-${STATUS_BADGE[inv.status] || "secondary"}`}>
                                            {inv.status}
                                        </span>
                                    </td>
                                    <td>
                                        <Link to={`/invoices/${inv.id}`} className="btn btn-outline-primary btn-sm">
                                            View
                                        </Link>
                                    </td>
                                </tr>
                            ))
                        )}

                    </tbody>

                </table>

            )}

        </DashboardLayout>
    );
}

export default InvoiceList;
