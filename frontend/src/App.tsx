import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Signup from './presenter/member/Signup';
import Dashboard from './DashBorad/DashBoard';
import SignIn from './presenter/member/Signin'

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
