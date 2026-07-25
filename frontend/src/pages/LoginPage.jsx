import { useState } from "react";

import { useAuth } from "../auth/AuthContext.jsx";

import "./LoginPage.css";

const emptyLoginForm = {
  email: "",
  password: "",
};

export default function LoginPage({
  onShowRegister,
}) {
  const { login, authLoading, authError } = useAuth();

  const [form, setForm] = useState(emptyLoginForm);
  const [submitError, setSubmitError] = useState("");

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    try {
      setSubmitError("");

      await login({
        email: form.email,
        password: form.password,
      });
    } catch (requestError) {
      setSubmitError(requestError.message);
    }
  }

  return (
    <main className="login-page">
      <section className="login-card">
        <p className="login-eyebrow">
          BIKE MAINTENANCE ADAPTER
        </p>

        <h1>Welcome back</h1>

        <p className="login-description">
          Log in to access the bike-maintenance system.
        </p>

        {(submitError || authError) && (
          <div className="login-error" role="alert">
            {submitError || authError}
          </div>
        )}

        <form
          className="login-form"
          onSubmit={handleSubmit}
        >
          <label htmlFor="email">
            Email
          </label>

          <input
            id="email"
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="admin@example.com"
            autoComplete="email"
            required
          />

          <label htmlFor="password">
            Password
          </label>

          <input
            id="password"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="Enter your password"
            autoComplete="current-password"
            required
          />

          <button
            type="submit"
            disabled={authLoading}
          >
            {authLoading ? "Logging in..." : "Log in"}
          </button>
        </form>

		
		<button
		  type="button"
		  className="auth-switch-button"
		  onClick={onShowRegister}
		>
		  Need an account? Register
		</button>

		
        <div className="development-accounts">
          <p>
            <strong>Development admin</strong>
          </p>

          <p>admin@example.com</p>
          <p>Admin123!</p>

          <p>
            <strong>Development technician</strong>
          </p>

          <p>technician@example.com</p>
          <p>Tech12345!</p>
        </div>
      </section>
    </main>
  );
}