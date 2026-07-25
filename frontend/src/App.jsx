import { useState } from "react";

import { useAuth } from "./auth/AuthContext.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import DashboardRouter from "./pages/DashboardRouter.jsx";


function App() {
    const { user, authLoading } = useAuth();
    const [authView, setAuthView] = useState("login");

    if (authLoading) {
        return (
            <main className="login-page">
                <section className="login-card">
                    <h1>Loading account...</h1>
                </section>
            </main>
        );
    }

	if (!user) {
	    if (authView === "register") {
	        return (
	            <RegisterPage
	                onShowLogin={() =>
	                    setAuthView("login")
	                }
	            />
	        );
	    }

	    return (
	        <LoginPage
	            onShowRegister={() =>
	                setAuthView("register")
	            }
	        />
	    );
	}

    return <DashboardRouter user={user} />;
}

export default App;