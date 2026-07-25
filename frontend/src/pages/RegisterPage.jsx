import { useState } from "react";

import { useAuth } from "../auth/AuthContext.jsx";

import "./LoginPage.css";

const emptyRegistrationForm = {
  displayName: "",
  email: "",
  password: "",
  confirmPassword: "",
};

export default function RegisterPage({
  onShowLogin,
}) {
  const {
    register,
    authLoading,
    authError,
  } = useAuth();

  const [form, setForm] =
    useState(emptyRegistrationForm);

  const [submitError, setSubmitError] =
    useState("");

  const [successMessage, setSuccessMessage] =
    useState("");

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
      setSuccessMessage("");

      if (form.password !== form.confirmPassword) {
        throw new Error("Passwords do not match");
      }

      await register({
        displayName: form.displayName,
        email: form.email,
        password: form.password,
      });

      setSuccessMessage(
        "Account created successfully. You can now log in.",
      );

      setForm(emptyRegistrationForm);
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

        <h1>Create account</h1>

        <p className="login-description">
          Create an account to report bike maintenance
          issues and track their status.
        </p>

        {(submitError || authError) && (
          <div
            className="login-error"
            role="alert"
          >
            {submitError || authError}
          </div>
        )}

        {successMessage && (
          <div
            className="registration-success"
            role="status"
          >
            {successMessage}
          </div>
        )}

        <form
          className="login-form"
          onSubmit={handleSubmit}
        >
          <label htmlFor="displayName">
            Display name
          </label>

          <input
            id="displayName"
            name="displayName"
            type="text"
            value={form.displayName}
            onChange={handleChange}
            placeholder="Your name"
            autoComplete="name"
            required
          />

          <label htmlFor="registerEmail">
            Email
          </label>

          <input
            id="registerEmail"
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="you@example.com"
            autoComplete="email"
            required
          />

          <label htmlFor="registerPassword">
            Password
          </label>

          <input
            id="registerPassword"
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="At least 8 characters"
            autoComplete="new-password"
            minLength="8"
            required
          />

          <label htmlFor="confirmPassword">
            Confirm password
          </label>

          <input
            id="confirmPassword"
            name="confirmPassword"
            type="password"
            value={form.confirmPassword}
            onChange={handleChange}
            placeholder="Enter the password again"
            autoComplete="new-password"
            minLength="8"
            required
          />

          <button
            type="submit"
            disabled={authLoading}
          >
            {authLoading
              ? "Creating account..."
              : "Create account"}
          </button>
        </form>

        <button
          type="button"
          className="auth-switch-button"
          onClick={onShowLogin}
        >
          Already have an account? Log in
        </button>
      </section>
    </main>
  );
}