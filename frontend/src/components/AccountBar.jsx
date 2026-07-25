import { useAuth } from "../auth/AuthContext.jsx";

import "./AccountBar.css";

export default function AccountBar() {
  const { user, logout } = useAuth();

  return (
    <section className="account-bar">
      <div>
        <p className="account-bar-label">
          Signed in as
        </p>

        <p className="account-bar-user">
          {user.displayName}
        </p>

        <p className="account-bar-role">
          {user.role}
        </p>
      </div>

      <button
        type="button"
        className="account-bar-logout"
        onClick={logout}
      >
        Log out
      </button>
    </section>
  );
}