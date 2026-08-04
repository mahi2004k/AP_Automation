import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { getDashboardData } from "../../api/dashboardApi";
import { toast } from "react-toastify";


function Dashboard() {


    const [dashboard, setDashboard] = useState(null);

    const [loading, setLoading] = useState(true);



    useEffect(() => {

        fetchDashboard();

    }, []);




    const fetchDashboard = async () => {


        try {


            const response = await getDashboardData();


            setDashboard(response.data);


        } catch (error) {


            toast.error(
                "Failed to load dashboard data"
            );


        } finally {


            setLoading(false);


        }


    };




    return (

        <DashboardLayout>


            <h2>

                Dashboard

            </h2>


            <hr />



            {
                loading ? (

                    <div className="text-center">

                        <div className="spinner-border text-primary">

                        </div>

                        <p>
                            Loading dashboard...
                        </p>

                    </div>


                ) : (



                    <div className="row">


                        <div className="col-md-3">


                            <div className="card p-3 shadow-sm">


                                <h5>
                                    Total Purchase Orders
                                </h5>


                                <h2>

                                    {dashboard?.totalPurchaseOrders || 0}

                                </h2>


                            </div>


                        </div>





                        <div className="col-md-3">


                            <div className="card p-3 shadow-sm">


                                <h5>
                                    Total Invoices
                                </h5>


                                <h2>

                                    {dashboard?.totalInvoices || 0}

                                </h2>


                            </div>


                        </div>





                        <div className="col-md-3">


                            <div className="card p-3 shadow-sm">


                                <h5>
                                    Pending Approvals
                                </h5>


                                <h2>

                                    {dashboard?.pendingInvoices || 0}

                                </h2>


                            </div>


                        </div>





                        <div className="col-md-3">


                            <div className="card p-3 shadow-sm">


                                <h5>
                                    Completed Payments
                                </h5>


                                <h2>

                                    0

                                </h2>


                            </div>


                        </div>



                    </div>


                )

            }



        </DashboardLayout>

    );

}


export default Dashboard;