import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getInvoiceById, fetchInvoiceFileBlobUrl } from "../../api/invoiceApi";
import { runInvoiceMatch } from "../../api/MatchingApi";
import { approveInvoice, rejectInvoice } from "../../api/approvalApi";
import { makePayment, getPaymentByInvoice } from "../../api/PaymentApi";
import { getInvoiceAuditHistory } from "../../api/auditApi";

function InvoiceDetail() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [invoice, setInvoice] = useState(null);
    const [payment, setPayment] = useState(null);
    const [auditLogs, setAuditLogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [actionLoading, setActionLoading] = useState(false);
    const [remarks, setRemarks] = useState("");
    const [paymentForm, setPaymentForm] = useState({
        paymentMethod: "BANK_TRANSFER",
        transactionReference: ""
    });

    const load = useCallback(async () => {

        setLoading(true);

        try {

            const [invRes, auditRes] = await Promise.all([
                getInvoiceById(id),
                getInvoiceAuditHistory(id)
            ]);

            setInvoice(invRes.data);
            setAuditLogs(auditRes.data);

            if (invRes.data.status === "PAID" || invRes.data.status === "APPROVED") {
                try {
                    const payRes = await getPaymentByInvoice(id);
                    setPayment(payRes.data);
                } catch {
                    setPayment(null);
                }
            }

        } catch (error) {

            toast.error("Failed to load invoice");
            navigate("/invoices", { replace: true });

        } finally {

            setLoading(false);

        }

    }, [id, navigate]);

    useEffect(() => {
        load();
    }, [load]);

    const handleViewPdf = async () => {

        try {
            const url = await fetchInvoiceFileBlobUrl(id);
            window.open(url, "_blank", "noopener,noreferrer");
        } catch (error) {
            toast.error("Could not open invoice PDF");
        }

    };

    const handleMatch = async () => {

        setActionLoading(true);

        try {

            const response = await runInvoiceMatch(id);

            toast[response.data.status === "MATCHED" ? "success" : "warning"](
                response.data.status === "MATCHED"
                    ? "3-way match successful"
                    : "Match completed with discrepancies — needs review"
            );

            load();

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Matching failed"
            );

        } finally {

            setActionLoading(false);

        }

    };

    const handleApprove = async () => {

        if (!remarks.trim()) {
            toast.error("Please enter approval remarks.");
            return;
        }

        setActionLoading(true);

        try {

            await approveInvoice(id, remarks.trim());
            toast.success("Invoice approved");
            setRemarks("");
            load();

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Approval failed"
            );

        } finally {

            setActionLoading(false);

        }

    };

    const handleReject = async () => {

        if (!remarks.trim()) {
            toast.error("Please enter a reason for rejection.");
            return;
        }

        setActionLoading(true);

        try {

            await rejectInvoice(id, remarks.trim());
            toast.success("Invoice rejected");
            setRemarks("");
            load();

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Rejection failed"
            );

        } finally {

            setActionLoading(false);

        }

    };

    const handlePay = async (e) => {

        e.preventDefault();

        if (!paymentForm.transactionReference.trim()) {
            toast.error("Please enter a transaction reference.");
            return;
        }

        setActionLoading(true);

        try {

            await makePayment(id, paymentForm);
            toast.success("Payment recorded successfully");
            load();

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Payment failed"
            );

        } finally {

            setActionLoading(false);

        }

    };

    if (loading || !invoice) {
        return (
            <DashboardLayout>
                <div className="text-center mt-5">
                    <div className="spinner-border text-primary" />
                    <p className="mt-3">Loading invoice...</p>
                </div>
            </DashboardLayout>
        );
    }

    const canMatch = ["EXTRACTED", "NEEDS_REVIEW"].includes(invoice.status);
    const canDecide = invoice.status === "MATCHED";
    const canPay = invoice.status === "APPROVED";

    return (
        <DashboardLayout>

            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Invoice {invoice.invoiceNumber || `#${invoice.id}`}</h2>
                <span className="badge bg-primary fs-6">{invoice.status}</span>
            </div>

            <div className="row">

                <div className="col-md-7">

                    <div className="card mb-4">
                        <div className="card-header d-flex justify-content-between align-items-center">
                            <span>Invoice Details</span>
                            {invoice.hasFile && (
                                <button className="btn btn-sm btn-outline-secondary" onClick={handleViewPdf}>
                                    View PDF
                                </button>
                            )}
                        </div>
                        <div className="card-body">
                            <dl className="row mb-0">
                                <dt className="col-sm-5">Vendor</dt>
                                <dd className="col-sm-7">{invoice.vendorName || "—"}</dd>

                                <dt className="col-sm-5">PO Number</dt>
                                <dd className="col-sm-7">{invoice.poNumber || "—"}</dd>

                                <dt className="col-sm-5">Invoice Date</dt>
                                <dd className="col-sm-7">{invoice.invoiceDate || "—"}</dd>

                                <dt className="col-sm-5">Tax Amount</dt>
                                <dd className="col-sm-7">{invoice.taxAmount != null ? `₹ ${Number(invoice.taxAmount).toFixed(2)}` : "—"}</dd>

                                <dt className="col-sm-5">Total Amount</dt>
                                <dd className="col-sm-7 fw-bold">{invoice.totalAmount != null ? `₹ ${Number(invoice.totalAmount).toFixed(2)}` : "—"}</dd>

                                <dt className="col-sm-5">Uploaded By</dt>
                                <dd className="col-sm-7">{invoice.uploadedBy || "—"}</dd>
                            </dl>
                        </div>
                    </div>

                    <div className="card mb-4">
                        <div className="card-header">Line Items</div>
                        <div className="card-body p-0">
                            <table className="table mb-0">
                                <thead className="table-light">
                                    <tr>
                                        <th>Description</th>
                                        <th>Qty</th>
                                        <th>Unit Price</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {(invoice.items || []).length === 0 ? (
                                        <tr><td colSpan="4" className="text-center py-3">No line items extracted</td></tr>
                                    ) : invoice.items.map((item, i) => (
                                        <tr key={i}>
                                            <td>{item.description}</td>
                                            <td>{item.quantity}</td>
                                            <td>{item.unitPrice}</td>
                                            <td>{item.amount}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className="card">
                        <div className="card-header">Audit Trail</div>
                        <ul className="list-group list-group-flush">
                            {auditLogs.length === 0 ? (
                                <li className="list-group-item text-muted">No history yet</li>
                            ) : auditLogs.map((log) => (
                                <li key={log.id} className="list-group-item">
                                    <div className="d-flex justify-content-between">
                                        <strong>{log.action}</strong>
                                        <small className="text-muted">{log.createdAt}</small>
                                    </div>
                                    <div className="small text-muted">by {log.username}</div>
                                    {log.details && <div>{log.details}</div>}
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>

                <div className="col-md-5">

                    {canMatch && (
                        <div className="card mb-4">
                            <div className="card-header">3-Way Match</div>
                            <div className="card-body">
                                <p className="text-muted">
                                    Compare this invoice against its purchase order and receiving report.
                                </p>
                                <button className="btn btn-primary w-100" disabled={actionLoading} onClick={handleMatch}>
                                    {actionLoading ? "Matching..." : "Run 3-Way Match"}
                                </button>
                            </div>
                        </div>
                    )}

                    {canDecide && (
                        <div className="card mb-4">
                            <div className="card-header">Approval Decision</div>
                            <div className="card-body">
                                <div className="mb-3">
                                    <label className="form-label">Remarks</label>
                                    <textarea
                                        className="form-control"
                                        rows="3"
                                        value={remarks}
                                        onChange={(e) => setRemarks(e.target.value)}
                                        placeholder="Reason for approval / rejection"
                                    />
                                </div>
                                <div className="d-flex gap-2">
                                    <button className="btn btn-success flex-fill" disabled={actionLoading} onClick={handleApprove}>
                                        Approve
                                    </button>
                                    <button className="btn btn-danger flex-fill" disabled={actionLoading} onClick={handleReject}>
                                        Reject
                                    </button>
                                </div>
                            </div>
                        </div>
                    )}

                    {canPay && (
                        <div className="card mb-4">
                            <div className="card-header">Make Payment</div>
                            <div className="card-body">
                                <form onSubmit={handlePay}>
                                    <div className="mb-3">
                                        <label className="form-label">Payment Method</label>
                                        <select
                                            className="form-select"
                                            value={paymentForm.paymentMethod}
                                            onChange={(e) => setPaymentForm({ ...paymentForm, paymentMethod: e.target.value })}
                                        >
                                            <option value="BANK_TRANSFER">Bank Transfer</option>
                                            <option value="UPI">UPI</option>
                                            <option value="CHEQUE">Cheque</option>
                                            <option value="CASH">Cash</option>
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Transaction Reference</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            value={paymentForm.transactionReference}
                                            onChange={(e) => setPaymentForm({ ...paymentForm, transactionReference: e.target.value })}
                                            required
                                        />
                                    </div>
                                    <button type="submit" className="btn btn-primary w-100" disabled={actionLoading}>
                                        {actionLoading ? "Processing..." : "Record Payment"}
                                    </button>
                                </form>
                            </div>
                        </div>
                    )}

                    {payment && (
                        <div className="card mb-4">
                            <div className="card-header">Payment Info</div>
                            <div className="card-body">
                                <dl className="row mb-0">
                                    <dt className="col-sm-6">Payment #</dt>
                                    <dd className="col-sm-6">{payment.paymentNumber}</dd>
                                    <dt className="col-sm-6">Amount</dt>
                                    <dd className="col-sm-6">₹ {Number(payment.amount).toFixed(2)}</dd>
                                    <dt className="col-sm-6">Date</dt>
                                    <dd className="col-sm-6">{payment.paymentDate}</dd>
                                    <dt className="col-sm-6">Method</dt>
                                    <dd className="col-sm-6">{payment.paymentMethod}</dd>
                                    <dt className="col-sm-6">Reference</dt>
                                    <dd className="col-sm-6">{payment.transactionReference}</dd>
                                    <dt className="col-sm-6">Status</dt>
                                    <dd className="col-sm-6">{payment.status}</dd>
                                </dl>
                            </div>
                        </div>
                    )}

                    {invoice.approvedBy && (
                        <div className="card">
                            <div className="card-header">Approval Info</div>
                            <div className="card-body">
                                <dl className="row mb-0">
                                    <dt className="col-sm-6">By</dt>
                                    <dd className="col-sm-6">{invoice.approvedBy}</dd>
                                    <dt className="col-sm-6">At</dt>
                                    <dd className="col-sm-6">{invoice.approvedAt}</dd>
                                    <dt className="col-sm-6">Remarks</dt>
                                    <dd className="col-sm-6">{invoice.approvalRemarks}</dd>
                                </dl>
                            </div>
                        </div>
                    )}

                </div>

            </div>

        </DashboardLayout>
    );
}

export default InvoiceDetail;
