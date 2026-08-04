import Navbar from "../components/Navbar";
import Sidebar from "../components/Sidebar";

function DashboardLayout({ children }) {

    return (

        <div>

            <Navbar />

            <div className="d-flex">

                <Sidebar />

                <div
                    className="flex-grow-1 p-4"
                    style={{
                        marginLeft: "250px",
                        marginTop: "60px",
                        minHeight: "100vh",
                        background: "#f8f9fa"
                    }}
                >

                    {children}

                </div>

            </div>

        </div>

    );

}

export default DashboardLayout;