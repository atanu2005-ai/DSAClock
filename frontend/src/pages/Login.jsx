import { useState } from 'react'
import {useNavigate, useNavigation} from "react-router-dom";

function Login() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const navigate = useNavigate()

    async function handleLogin(event) {
        event.preventDefault()

        const credentials = {
            email,
            password
        }

        const response = await
            fetch('http://localhost:8080/api/login', { //fetch post user endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(credentials)
            })

        const data = await response.json();

        localStorage.setItem('token', data.token)

        navigate('/problems') //goes to problems page after login
    }

    return (
        <main className="auth-page">
            <div className="auth-card">
                <h1>Welcome back</h1>
                <p>Enter your credentials to login</p>

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            placeholder="Enter your email"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                            placeholder="Enter your password"
                        />
                    </div>

                        <button type="submit">Login</button>

                </form>
            </div>
        </main>
    )
}

export default Login