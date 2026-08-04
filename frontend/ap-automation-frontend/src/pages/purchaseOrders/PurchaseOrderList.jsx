import { useEffect, useState } from "react";
import { getPurchaseOrders, deletePurchaseOrder } from "../../api/purchaseOrderApi";
import DashboardLayout from "../../layouts/DashboardLayout";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";

function PurchaseOrderList() {

    const [purchaseOrders, setPurchaseOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletingId, setDeletingId] = useState(null);

    const loadPurchaseOrders = async () => {

        try {

            const response = await getPurchaseOrders();

            setPurchaseOrders(response.data);

        } catch (error) {

            toast.error("Failed to load Purchase Orders");

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {
        loadPurchaseOrders();
    }, []);

    const handleDelete = async (po) => {

        if (!window.confirm(`Delete purchase order ${po.poNumber}? This cannot be undone.`)) {
            return;
        }

        setDeletingId(po.id);

        try {

            await deletePurchaseOrder(po.id);

            toast.success("Purchase Order deleted");

            setPurchaseOrders((prev) => prev.filter((p) => p.id !== po.id));

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                error.response?.data ||
                "Failed to delete Purchase Order"
            );

        } finally {

            setDeletingId(null);

        }

    };

    return (

        <DashboardLayout>

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2>Purchase Orders</h2>

                <Link
                    to="/purchase-orders/add"
                    className="btn btn-primary"
                >
                    + Add Purchase Order
                </Link>

            </div>

            {loading ? (

                <div className="text-center">

                    Loading...

                </div>

            ) : (

                <table className="table table-bordered table-hover">

                    <thead className="table-dark">

                        <tr>

                            <th>ID</th>
                            <th>PO Number</th>
                            <th>Vendor</th>
                            <th>Status</th>
                            <th>Items</th>
                            <th>Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {purchaseOrders.length === 0 ? (

                            <tr>

                                <td colSpan="6" className="text-center">

                                    No Purchase Orders Found

                                </td>

                            </tr>

                        ) : (

                            purchaseOrders.map((po) => (

                                <tr key={po.id}>

                                    <td>{po.id}</td>

                                    <td>{po.poNumber}</td>

                                    <td>{po.vendorName}</td>

                                    <td>{po.status}</td>

                                    <td>{po.items?.length || 0}</td>

                                    <td>

                                   <Link
                                        to={`/purchase-orders/edit/${po.id}`}
                                        className="btn btn-warning btn-sm me-2"
                                    >
                                        Edit
                                    </Link>

                                        <button
                                            className="btn btn-danger btn-sm"
                                            disabled={deletingId === po.id}
                                            onClick={() => handleDelete(po)}
                                        >
                                            {deletingId === po.id ? "Deleting..." : "Delete"}
                                        </button>

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

export default PurchaseOrderList;
