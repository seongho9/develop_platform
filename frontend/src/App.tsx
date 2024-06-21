import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Signup from './present/member/Signup';
import SignIn from './present/member/Signin'
import DashBoradView from './view/DashBoardView';
import Dashboard from './present/development/Dashboard';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignIn />}></Route>
          <Route path="/register" element={<Signup />}></Route>
          <Route path="/dashboard" element={<Dashboard />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
