import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import PurchaseOrderForm from "../../components/PurchaseOrderForm";

import {
    getPurchaseOrderById,
    updatePurchaseOrder
} from "../../api/purchaseOrderApi";

function EditPurchaseOrder() {

    const { id } = useParams();

    const navigate = useNavigate();

    const [purchaseOrder, setPurchaseOrder] = useState(null);

    const [loading, setLoading] = useState(true);

    useEffect(() => {

        loadPurchaseOrder();

    }, [id]);

    const loadPurchaseOrder = async () => {

        try {

            const response = await getPurchaseOrderById(id);

            setPurchaseOrder(response.data);

        } catch (error) {

            toast.error(
                error.response?.data?.message ||
                "Failed to load Purchase Order"
            );

            navigate("/purchase-orders", { replace: true });

        } finally {

            setLoading(false);

        }

    };

    const handleUpdate = async (form) => {

        try {

            await updatePurchaseOrder(id, form);

            toast.success(
                "Purchase Order Updated Successfully"
            );

            navigate("/purchase-orders");

        } catch (error) {

            toast.error(

                error.response?.data?.message ||

                "Update Failed"

            );

            throw error;

        }

    };

    if (loading) {

        return (

            <DashboardLayout>

                <div className="text-center mt-5">

                    <div className="spinner-border text-primary">

                    </div>

                    <p className="mt-3">

                        Loading Purchase Order...

                    </p>

                </div>

            </DashboardLayout>

        );

    }

    return (

        <DashboardLayout>

            <PurchaseOrderForm

                title="Edit Purchase Order"

                initialData={purchaseOrder}

                onSubmit={handleUpdate}

            />

        </DashboardLayout>

    );

}

export default EditPurchaseOrder;