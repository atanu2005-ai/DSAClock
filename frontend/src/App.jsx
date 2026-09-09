import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Profile from "./pages/Profile.jsx";
import Problems from "./pages/Problems.jsx";
import ProblemDetails from "./pages/ProblemDetails.jsx"
import Register from './pages/Register.jsx'
import RegisterSuccess from "./pages/RegisterSuccess.jsx";
import Navbar from "./components/Navbar.jsx";

function App() {
  return (
      <BrowserRouter>
          <Navbar/>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
            <Route path="/profile" element={<Profile />} />
          <Route path={"/register-success"} element={<RegisterSuccess/>}/>
            <Route path={"/problems"} element={<Problems/>}/>
            <Route path={"/problems/:problemId"} element={<ProblemDetails/>}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App