import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import { getPurchaseOrders } from "../../api/purchaseOrderApi";
import { createReceivingReport } from "../../api/receivingReportApi";

function AddReceivingReport() {

    const navigate = useNavigate();

    const [purchaseOrders, setPurchaseOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [form, setForm] = useState({
        reportNumber: "",
        receivedDate: "",
        purchaseOrderId: "",
        items: [{ description: "", quantityReceived: 1 }]
    });

    useEffect(() => {

        (async () => {
            try {
                const response = await getPurchaseOrders();
                setPurchaseOrders(response.data);
            } catch {
                toast.error("Failed to load purchase orders");
            } finally {
                setLoading(false);
            }
        })();

    }, []);

    const handleItemChange = (index, field, value) => {

        const items = [...form.items];
        items[index] = {
            ...items[index],
            [field]: field === "quantityReceived" ? Number(value) : value
        };
        setForm({ ...form, items });

    };

    const addItem = () => {
        setForm({
            ...form,
            items: [...form.items, { description: "", quantityReceived: 1 }]
        });
    };

    const removeItem = (index) => {
        if (form.items.length === 1) {
            toast.warning("At least one item is required.");
            return;
        }
        setForm({ ...form, items: form.items.filter((_, i) => i !== index) });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        if (!form.purchaseOrderId) {
            toast.error("Please select a purchase order.");
            return;
        }

        setSaving(true);

        try {

            await createReceivingReport({
                ...form,
                purchaseOrderId: Number(form.purchaseOrderId)
            });

            toast.success("Receiving report created");
            navigate("/receiving-reports");

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to create receiving report"
            );

        } finally {

            setSaving(false);

        }

    };

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

            <h2 className="mb-4">Add Receiving Report</h2>

            <form onSubmit={handleSubmit}>

                <div className="card mb-4">
                    <div className="card-header">Report Details</div>
                    <div className="card-body">
                        <div className="row">
                            <div className="col-md-4 mb-3">
                                <label className="form-label">Report Number</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    value={form.reportNumber}
                                    onChange={(e) => setForm({ ...form, reportNumber: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label">Received Date</label>
                                <input
                                    type="date"
                                    className="form-control"
                                    value={form.receivedDate}
                                    onChange={(e) => setForm({ ...form, receivedDate: e.target.value })}
                                    required
                                />
                            </div>
                            <div className="col-md-4 mb-3">
                                <label className="form-label">Purchase Order</label>
                                <select
                                    className="form-select"
                                    value={form.purchaseOrderId}
                                    onChange={(e) => setForm({ ...form, purchaseOrderId: e.target.value })}
                                    required
                                >
                                    <option value="">Select a purchase order</option>
                                    {purchaseOrders.map((po) => (
                                        <option key={po.id} value={po.id}>
                                            {po.poNumber} — {po.vendorName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="card">
                    <div className="card-header d-flex justify-content-between align-items-center">
                        <span>Items Received</span>
                        <button type="button" className="btn btn-success btn-sm" onClick={addItem}>
                            + Add Item
                        </button>
                    </div>
                    <div className="card-body">
                        <table className="table table-bordered align-middle">
                            <thead className="table-light">
                                <tr>
                                    <th>Description</th>
                                    <th width="150">Quantity Received</th>
                                    <th width="100">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {form.items.map((item, index) => (
                                    <tr key={index}>
                                        <td>
                                            <input
                                                type="text"
                                                className="form-control"
                                                value={item.description}
                                                onChange={(e) => handleItemChange(index, "description", e.target.value)}
                                                required
                                            />
                                        </td>
                                        <td>
                                            <input
                                                type="number"
                                                min="1"
                                                className="form-control"
                                                value={item.quantityReceived}
                                                onChange={(e) => handleItemChange(index, "quantityReceived", e.target.value)}
                                                required
                                            />
                                        </td>
                                        <td className="text-center">
                                            <button
                                                type="button"
                                                className="btn btn-danger btn-sm"
                                                onClick={() => removeItem(index)}
                                            >
                                                Remove
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="mt-4 d-flex gap-2">
                    <button type="submit" className="btn btn-primary" disabled={saving}>
                        {saving ? "Saving..." : "Save Receiving Report"}
                    </button>
                    <button type="button" className="btn btn-secondary" onClick={() => navigate("/receiving-reports")}>
                        Cancel
                    </button>
                </div>

            </form>

        </DashboardLayout>
    );
}

export default AddReceivingReport;
