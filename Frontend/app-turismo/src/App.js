// src/App.js (actualizado)
import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import Register from './components/Register';
import CompanyLogin from './components/CompanyLogin';
import CompanyRegister from './components/CompanyRegister';
import Dashboard from './components/Dashboard';
import './App.css';

function App() {
  const [authToken, setAuthToken] = useState(localStorage.getItem('authToken') || null);
  const [userInfo, setUserInfo] = useState(JSON.parse(localStorage.getItem('userInfo') || '{}'));
  const [userRole, setUserRole] = useState(localStorage.getItem('userRole') || 'USER');
  const [authView, setAuthView] = useState('userLogin'); // 'userLogin', 'userRegister', 'companyLogin', 'companyRegister'

  useEffect(() => {
    if (authToken) {
      setAuthView('dashboard');
    } else {
      setAuthView('userLogin');
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
    setAuthView('userLogin');
  };

  const navigateTo = (view) => {
    setAuthView(view);
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
          {authView === 'userLogin' && (
            <Login 
              onLogin={handleLogin} 
              onToggleRegister={() => navigateTo('userRegister')}
              onSwitchToCompany={() => navigateTo('companyLogin')}
            />
          )}
          
          {authView === 'userRegister' && (
            <Register 
              onLogin={handleLogin}
              onToggleLogin={() => navigateTo('userLogin')}
              onSwitchToCompany={() => navigateTo('companyRegister')}
            />
          )}
          
          {authView === 'companyLogin' && (
            <CompanyLogin 
              onLogin={handleLogin} 
              onToggleRegister={() => navigateTo('companyRegister')}
              onSwitchToUser={() => navigateTo('userLogin')}
            />
          )}
          
          {authView === 'companyRegister' && (
            <CompanyRegister 
              onLogin={handleLogin}
              onToggleLogin={() => navigateTo('companyLogin')}
              onSwitchToUser={() => navigateTo('userRegister')}
            />
          )}
        </div>
      )}
    </div>
  );
}

export default App;