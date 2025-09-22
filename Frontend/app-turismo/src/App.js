// src/App.js
import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import './App.css';

function App() {
  const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);
  const [userInfo, setUserInfo] = useState(JSON.parse(localStorage.getItem('userInfo') || '{}'));
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole') || 'USER');
  const [showLogin, setShowLogin] = useState(true);
  const [loginType, setLoginType] = useState('user'); // 'user' o 'company'

  useEffect(() => {
    if (authToken) {
      setShowLogin(false);
    } else {
      setShowLogin(true);
    }
  }, [authToken]);

  const handleLogin = (token, user, role) => {
    setAuthToken(token);
    setUserInfo(user);
    setUserRole(role);
    localStorage.setItem('authToken', token);
    localStorage.setItem('userInfo', JSON.stringify(user));
    localStorage.setItem('userRole', role);
  };

  const handleLogout = () => {
    setAuthToken(null);
    setUserInfo({});
    setUserRole('USER');
    localStorage.removeItem('authToken');
    localStorage.removeItem('userInfo');
    localStorage.removeItem('userRole');
  };

  const toggleLoginRegister = () => {
    setShowLogin(!showLogin);
  };

  const toggleLoginType = (type) => {
    setLoginType(type);
  };

  return (
    <div className="app-container">
      {authToken ? (
        <Dashboard 
          userInfo={userInfo} 
          userRole={userRole} 
          onLogout={handleLogout} 
        />
      ) : (
        <div className="auth-container">
          {showLogin ? (
            <Login 
              onLogin={handleLogin} 
              onToggleRegister={toggleLoginRegister}
              loginType={loginType}
              onToggleLoginType={toggleLoginType}
            />
          ) : (
            <Register 
              onLogin={handleLogin}
              onToggleLogin={toggleLoginRegister}
              loginType={loginType}
              onToggleLoginType={toggleLoginType}
            />
          )}
        </div>
      )}
    </div>
  );
}

export default App;