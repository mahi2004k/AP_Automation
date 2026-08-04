import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function PurchaseOrderForm({
    initialData,
    onSubmit,
    title
}) {

    const navigate = useNavigate();

    const [loading, setLoading] = useState(false);

    const [form, setForm] = useState(
        initialData || {
            poNumber: "",
            vendorName: "",
            items: [
                {
                    description: "",
                    quantity: 1,
                    unitPrice: 0
                }
            ]
        }
    );

    useEffect(() => {

        if (initialData) {

            setForm(initialData);

        }

    }, [initialData]);

    const handleChange = (e) => {

        setForm({

            ...form,

            [e.target.name]: e.target.value

        });

    };

    const handleItemChange = (index, field, value) => {

        const updatedItems = [...form.items];

        updatedItems[index] = {

            ...updatedItems[index],

            [field]:
                field === "quantity" || field === "unitPrice"
                    ? Number(value)
                    : value

        };

        setForm({

            ...form,

            items: updatedItems

        });

    };

    const addItem = () => {

        setForm({

            ...form,

            items: [

                ...form.items,

                {
                    description: "",
                    quantity: 1,
                    unitPrice: 0
                }

            ]

        });

    };

    const removeItem = (index) => {

        if (form.items.length === 1) {

            toast.warning("At least one item is required.");

            return;

        }

        setForm({

            ...form,

            items: form.items.filter((_, i) => i !== index)

        });

    };

    const grandTotal = form.items.reduce(

        (total, item) =>

            total + (Number(item.quantity) * Number(item.unitPrice)),

        0

    );

    const handleSubmit = async (e) => {

        e.preventDefault();

        const payload = {

            ...form,

            poNumber: form.poNumber.trim(),

            vendorName: form.vendorName.trim(),

            items: form.items.map(item => ({

                description: item.description.trim(),

                quantity: Number(item.quantity),

                unitPrice: Number(item.unitPrice)

            }))

        };

        if (payload.items.some(item => item.description === "")) {

            toast.error("Item description cannot be empty.");

            return;

        }

        setLoading(true);

        try {

            await onSubmit(payload);

        } catch (error) {

            toast.error(

                error.response?.data?.message ||

                "Failed to save Purchase Order"

            );

        } finally {

            setLoading(false);

        }

    };

    return (

        <>

            <h2 className="mb-4">

                {title}

            </h2>

            <form onSubmit={handleSubmit}>

                <div className="card mb-4">

                    <div className="card-header">

                        Purchase Order Details

                    </div>

                    <div className="card-body">

                        <div className="row">

                            <div className="col-md-6 mb-3">

                                <label className="form-label">

                                    PO Number

                                </label>

                                <input
                                    type="text"
                                    name="poNumber"
                                    className="form-control"
                                    value={form.poNumber}
                                    onChange={handleChange}
                                    required
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <label className="form-label">

                                    Vendor Name

                                </label>

                                <input
                                    type="text"
                                    name="vendorName"
                                    className="form-control"
                                    value={form.vendorName}
                                    onChange={handleChange}
                                    required
                                />

                            </div>

                        </div>

                    </div>

                </div>

                <div className="card">

                    <div className="card-header d-flex justify-content-between align-items-center">

                        <span>

                            Purchase Order Items

                        </span>

                        <button
                            type="button"
                            className="btn btn-success btn-sm"
                            onClick={addItem}
                        >

                            + Add Item

                        </button>

                    </div>

                    <div className="card-body">

                        <table className="table table-bordered align-middle">

                            <thead className="table-light">

                                <tr>

                                    <th>Description</th>
                                    <th width="150">Quantity</th>
                                    <th width="180">Unit Price</th>
                                    <th width="180">Total</th>
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
                                                onChange={(e) =>
                                                    handleItemChange(
                                                        index,
                                                        "description",
                                                        e.target.value
                                                    )
                                                }
                                                required
                                            />

                                        </td>

                                        <td>

                                            <input
                                                type="number"
                                                min="1"
                                                className="form-control"
                                                value={item.quantity}
                                                onChange={(e) =>
                                                    handleItemChange(
                                                        index,
                                                        "quantity",
                                                        e.target.value
                                                    )
                                                }
                                                required
                                            />

                                        </td>

                                        <td>

                                            <input
                                                type="number"
                                                min="0"
                                                step="0.01"
                                                className="form-control"
                                                value={item.unitPrice}
                                                onChange={(e) =>
                                                    handleItemChange(
                                                        index,
                                                        "unitPrice",
                                                        e.target.value
                                                    )
                                                }
                                                required
                                            />

                                        </td>

                                        <td className="fw-bold">

                                            ₹ {(Number(item.quantity) * Number(item.unitPrice)).toFixed(2)}

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

                        <div className="text-end mt-3">

                            <h4>

                                Grand Total : ₹ {grandTotal.toFixed(2)}

                            </h4>

                        </div>

                    </div>

                </div>

                <div className="mt-4 d-flex gap-2">

                    <button
                        type="submit"
                        className="btn btn-primary"
                        disabled={loading}
                    >

                        {loading ? "Saving..." : "Save Purchase Order"}

                    </button>

                    <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={() => navigate("/purchase-orders")}
                    >

                        Cancel

                    </button>

                </div>

            </form>

        </>

    );

}

export default PurchaseOrderForm;