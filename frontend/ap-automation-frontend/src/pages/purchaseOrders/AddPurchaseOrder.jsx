import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

import DashboardLayout from "../../layouts/DashboardLayout";
import PurchaseOrderForm from "../../components/PurchaseOrderForm";

import { createPurchaseOrder } from "../../api/purchaseOrderApi";

function AddPurchaseOrder() {

    const navigate = useNavigate();

    const handleCreate = async (form) => {

        try {

            await createPurchaseOrder(form);

            toast.success(
                "Purchase Order Created Successfully"
            );

            navigate("/purchase-orders");

        } catch (error) {

            toast.error(

                error.response?.data?.message ||

                "Failed to create Purchase Order"

            );

            throw error;

        }

    };

    return (

        <DashboardLayout>

            <PurchaseOrderForm

                title="Add Purchase Order"

                onSubmit={handleCreate}

            />

        </DashboardLayout>

    );

}

export default AddPurchaseOrder;