import {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

import {
  getCurrentUser,
  loadAuthToken,
  loginAccount,
  registerAccount,
  removeAuthToken,
  saveAuthToken,
} from "../api/authApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() =>
    loadAuthToken(),
  );

  const [user, setUser] = useState(null);
  const [authLoading, setAuthLoading] =
    useState(true);
  const [authError, setAuthError] =
    useState("");

  useEffect(() => {
    let cancelled = false;

    async function restoreSession() {
      if (!token) {
        setUser(null);
        setAuthLoading(false);
        return;
      }

      try {
        setAuthLoading(true);
        setAuthError("");

        const currentUser =
          await getCurrentUser(token);

        if (!cancelled) {
          setUser(currentUser);
        }
      } catch (requestError) {
        if (!cancelled) {
          removeAuthToken();
          setToken(null);
          setUser(null);
          setAuthError(requestError.message);
        }
      } finally {
        if (!cancelled) {
          setAuthLoading(false);
        }
      }
    }

    restoreSession();

    return () => {
      cancelled = true;
    };
  }, [token]);

  async function login(credentials) {
    try {
      setAuthLoading(true);
      setAuthError("");

      const loginResponse =
        await loginAccount(credentials);

      saveAuthToken(loginResponse.token);

      setToken(loginResponse.token);
      setUser(loginResponse.user);

      return loginResponse.user;
    } catch (requestError) {
      setAuthError(requestError.message);
      throw requestError;
    } finally {
      setAuthLoading(false);
    }
  }

  async function register(registration) {
    try {
      setAuthLoading(true);
      setAuthError("");

      return await registerAccount(registration);
    } catch (requestError) {
      setAuthError(requestError.message);
      throw requestError;
    } finally {
      setAuthLoading(false);
    }
  }

  function logout() {
    removeAuthToken();
    setToken(null);
    setUser(null);
    setAuthError("");
  }

  const contextValue = {
    token,
    user,
    authLoading,
    authError,
    isAuthenticated: Boolean(token && user),
    login,
    register,
    logout,
  };

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (context === null) {
    throw new Error(
      "useAuth must be used inside AuthProvider",
    );
  }

  return context;
}