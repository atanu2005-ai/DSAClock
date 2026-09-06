import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Problems from "./pages/Problems.jsx";
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
          <Route path={"/register-success"} element={<RegisterSuccess/>}/>
            <Route path={"/problems"} element={<Problems/>}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App