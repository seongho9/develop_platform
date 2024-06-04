import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import SignIn from './member/Signin';
import SignUp from './member/Signup';
import Dashboard from './DashBorad/DashBoard';
import UserInfo from './member/UserInfo';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<SignIn />}></Route>
          <Route path="/register" element={<SignUp />}></Route>
          <Route path="/dashboard" element={<Dashboard />}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
