import AdminDashboard from "./AdminDashboard.jsx";
import UserDashboard from "./UserDashboard.jsx";
import TechnicianDashboard from "./TechnicianDashboard.jsx";

function DashboardRouter({ user }) {
    switch (user.role) {
        case "ADMIN":
            return <AdminDashboard />;

        case "TECHNICIAN":
            return <TechnicianDashboard />;

        case "USER":
            return <UserDashboard />;

        default:
            return (
                <main className="app">
                    <h1>Access unavailable</h1>
                    <p>Your account does not have a recognized role.</p>
                </main>
            );
    }
}

export default DashboardRouter;