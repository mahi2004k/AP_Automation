import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getInvoices } from "../../api/invoiceApi";
import { runInvoiceMatch } from "../../api/MatchingApi";

function MatchingList() {

    const [invoices, setInvoices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [matchingId, setMatchingId] = useState(null);
    const [results, setResults] = useState({});

    const load = async () => {

        setLoading(true);

        try {

            const [extracted, needsReview] = await Promise.all([
                getInvoices("EXTRACTED"),
                getInvoices("NEEDS_REVIEW")
            ]);

            setInvoices([...extracted.data, ...needsReview.data]);

        } catch (error) {

            toast.error("Failed to load invoices awaiting matching");

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => { load(); }, []);

    const handleMatch = async (invoiceId) => {

        setMatchingId(invoiceId);

        try {

            const response = await runInvoiceMatch(invoiceId);
            setResults((prev) => ({ ...prev, [invoiceId]: response.data }));

            toast[response.data.status === "MATCHED" ? "success" : "warning"](
                response.data.status === "MATCHED"
                    ? `Invoice #${invoiceId} matched successfully`
                    : `Invoice #${invoiceId} needs review — discrepancies found`
            );

            load();

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Matching failed. Make sure a Purchase Order and Receiving Report exist for this invoice."
            );

        } finally {

            setMatchingId(null);

        }

    };

    return (
        <DashboardLayout>

            <h2 className="mb-4">Three-Way Matching</h2>

            <p className="text-muted">
                These invoices have been extracted but not yet matched against their
                purchase order and receiving report.
            </p>

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
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {invoices.length === 0 ? (
                            <tr><td colSpan="7" className="text-center">Nothing waiting to be matched 🎉</td></tr>
                        ) : invoices.map((inv) => {

                            const result = results[inv.id];

                            return (
                                <tr key={inv.id}>
                                    <td>{inv.id}</td>
                                    <td>{inv.invoiceNumber || "—"}</td>
                                    <td>{inv.vendorName || "—"}</td>
                                    <td>{inv.poNumber || "—"}</td>
                                    <td>{inv.totalAmount != null ? `₹ ${Number(inv.totalAmount).toFixed(2)}` : "—"}</td>
                                    <td>
                                        <span className="badge bg-secondary">{inv.status}</span>
                                        {result && (
                                            <div className="small mt-1">
                                                {result.status === "MATCHED" ? (
                                                    <span className="text-success">✓ Matched</span>
                                                ) : (
                                                    <span className="text-warning">⚠ {result.remarks}</span>
                                                )}
                                            </div>
                                        )}
                                    </td>
                                    <td>
                                        <button
                                            className="btn btn-primary btn-sm me-2"
                                            disabled={matchingId === inv.id}
                                            onClick={() => handleMatch(inv.id)}
                                        >
                                            {matchingId === inv.id ? "Matching..." : "Run Match"}
                                        </button>
                                        <Link to={`/invoices/${inv.id}`} className="btn btn-outline-secondary btn-sm">
                                            View
                                        </Link>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            )}

        </DashboardLayout>
    );
}

export default MatchingList;
