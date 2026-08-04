import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getReceivingReports, deleteReceivingReport } from "../../api/receivingReportApi";

function ReceivingReportList() {

    const [reports, setReports] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletingId, setDeletingId] = useState(null);

    const load = async () => {

        try {
            const response = await getReceivingReports();
            setReports(response.data);
        } catch (error) {
            toast.error("Failed to load receiving reports");
        } finally {
            setLoading(false);
        }

    };

    useEffect(() => { load(); }, []);

    const handleDelete = async (report) => {

        if (!window.confirm(`Delete receiving report ${report.reportNumber}?`)) return;

        setDeletingId(report.id);

        try {
            await deleteReceivingReport(report.id);
            toast.success("Receiving report deleted");
            setReports((prev) => prev.filter((r) => r.id !== report.id));
        } catch (error) {
            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to delete receiving report"
            );
        } finally {
            setDeletingId(null);
        }

    };

    return (
        <DashboardLayout>

            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Receiving Reports</h2>
                <Link to="/receiving-reports/add" className="btn btn-primary">
                    + Add Receiving Report
                </Link>
            </div>

            {loading ? (
                <div className="text-center">Loading...</div>
            ) : (
                <table className="table table-bordered table-hover">
                    <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Report #</th>
                            <th>Purchase Order ID</th>
                            <th>Received Date</th>
                            <th>Items</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {reports.length === 0 ? (
                            <tr><td colSpan="6" className="text-center">No receiving reports found</td></tr>
                        ) : reports.map((r) => (
                            <tr key={r.id}>
                                <td>{r.id}</td>
                                <td>{r.reportNumber}</td>
                                <td>{r.purchaseOrderId}</td>
                                <td>{r.receivedDate}</td>
                                <td>{r.items?.length || 0}</td>
                                <td>
                                    <button
                                        className="btn btn-danger btn-sm"
                                        disabled={deletingId === r.id}
                                        onClick={() => handleDelete(r)}
                                    >
                                        {deletingId === r.id ? "Deleting..." : "Delete"}
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

        </DashboardLayout>
    );
}

export default ReceivingReportList;
