import { createContext, useState, useEffect, useContext } from 'react';
import api from '@/api/axios';

export const AuthContext = createContext(null);

export default function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(null);

  // WHY this useEffect exists:
  // When the user refreshes the page, React state resets to initial values.
  // token gets restored from localStorage (line above), but user becomes null.
  // This effect runs whenever token changes — if token exists but user is missing,
  // it re-fetches the user from the backend to repopulate the context.
  useEffect(() => {
    if (token && !user) {
      api
        .get('/auth/me')
        .then((res) => setUser(res.data))
        .catch(() => {
          // token is invalid or expired — clean up and force re-login
          localStorage.removeItem('token');
          setToken(null);
        });
    }
  }, [token]);

  const login = (jwt, userData) => {
    localStorage.setItem('token', jwt);
    setToken(jwt);
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
