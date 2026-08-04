import { useEffect, useState } from "react";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getInvoices } from "../../api/invoiceApi";
import { getInvoiceAuditHistory } from "../../api/auditApi";

function AuditLogList() {

    const [invoices, setInvoices] = useState([]);
    const [selectedId, setSelectedId] = useState("");
    const [logs, setLogs] = useState([]);
    const [loadingInvoices, setLoadingInvoices] = useState(true);
    const [loadingLogs, setLoadingLogs] = useState(false);

    useEffect(() => {

        (async () => {
            try {
                const response = await getInvoices();
                setInvoices(response.data);
            } catch {
                toast.error("Failed to load invoices");
            } finally {
                setLoadingInvoices(false);
            }
        })();

    }, []);

    useEffect(() => {

        if (!selectedId) {
            setLogs([]);
            return;
        }

        (async () => {
            setLoadingLogs(true);
            try {
                const response = await getInvoiceAuditHistory(selectedId);
                setLogs(response.data);
            } catch {
                toast.error("Failed to load audit history");
            } finally {
                setLoadingLogs(false);
            }
        })();

    }, [selectedId]);

    return (
        <DashboardLayout>

            <h2 className="mb-4">Audit Log</h2>

            <div className="mb-4" style={{ maxWidth: 420 }}>
                <label className="form-label">Select an invoice to view its history</label>
                <select
                    className="form-select"
                    value={selectedId}
                    disabled={loadingInvoices}
                    onChange={(e) => setSelectedId(e.target.value)}
                >
                    <option value="">-- Select Invoice --</option>
                    {invoices.map((inv) => (
                        <option key={inv.id} value={inv.id}>
                            #{inv.id} — {inv.invoiceNumber || "Untitled"} ({inv.vendorName || "Unknown vendor"})
                        </option>
                    ))}
                </select>
            </div>

            {loadingLogs ? (
                <div className="text-center">Loading audit trail...</div>
            ) : selectedId ? (
                <div className="card">
                    <div className="card-header">Audit Trail for Invoice #{selectedId}</div>
                    <ul className="list-group list-group-flush">
                        {logs.length === 0 ? (
                            <li className="list-group-item text-muted">No history recorded</li>
                        ) : logs.map((log) => (
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
            ) : (
                <div className="text-muted">Choose an invoice above to see its full history.</div>
            )}

        </DashboardLayout>
    );
}

export default AuditLogList;
