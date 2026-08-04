import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getPayments } from "../../api/PaymentApi";
import { getInvoices } from "../../api/invoiceApi";

function PaymentList() {

    const [payments, setPayments] = useState([]);
    const [approvedInvoices, setApprovedInvoices] = useState([]);
    const [loading, setLoading] = useState(true);

    const load = async () => {

        setLoading(true);

        try {

            const [paymentsRes, approvedRes] = await Promise.all([
                getPayments(),
                getInvoices("APPROVED")
            ]);

            setPayments(paymentsRes.data);
            setApprovedInvoices(approvedRes.data);

        } catch (error) {

            toast.error("Failed to load payments");

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => { load(); }, []);

    if (loading) {
        return (
            <DashboardLayout>
                <div className="text-center mt-5">
                    <div className="spinner-border text-primary" />
                </div>
            </DashboardLayout>
        );
    }

    return (
        <DashboardLayout>

            <h2 className="mb-4">Payments</h2>

            {approvedInvoices.length > 0 && (
                <div className="card mb-4">
                    <div className="card-header">Approved Invoices Ready for Payment</div>
                    <div className="card-body p-0">
                        <table className="table mb-0">
                            <thead className="table-light">
                                <tr>
                                    <th>Invoice #</th>
                                    <th>Vendor</th>
                                    <th>Total</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {approvedInvoices.map((inv) => (
                                    <tr key={inv.id}>
                                        <td>{inv.invoiceNumber}</td>
                                        <td>{inv.vendorName}</td>
                                        <td>₹ {Number(inv.totalAmount || 0).toFixed(2)}</td>
                                        <td>
                                            <Link to={`/invoices/${inv.id}`} className="btn btn-primary btn-sm">
                                                Pay Now
                                            </Link>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}

            <div className="card">
                <div className="card-header">Payment History</div>
                <div className="card-body p-0">
                    <table className="table mb-0">
                        <thead className="table-dark">
                            <tr>
                                <th>Payment #</th>
                                <th>Invoice ID</th>
                                <th>Amount</th>
                                <th>Method</th>
                                <th>Reference</th>
                                <th>Date</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {payments.length === 0 ? (
                                <tr><td colSpan="7" className="text-center py-3">No payments recorded yet</td></tr>
                            ) : payments.map((p) => (
                                <tr key={p.id}>
                                    <td>{p.paymentNumber}</td>
                                    <td>{p.invoiceId}</td>
                                    <td>₹ {Number(p.amount).toFixed(2)}</td>
                                    <td>{p.paymentMethod}</td>
                                    <td>{p.transactionReference}</td>
                                    <td>{p.paymentDate}</td>
                                    <td><span className="badge bg-success">{p.status}</span></td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

        </DashboardLayout>
    );
}

export default PaymentList;
