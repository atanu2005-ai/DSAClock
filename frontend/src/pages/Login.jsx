import { useEffect, useState } from 'react'
import {Link, useNavigate, useNavigation} from "react-router-dom";

function Login() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState("")
    const [googleError, setGoogleError] = useState("")

    const navigate = useNavigate()

    const params = new URLSearchParams(window.location.search);
    const errorForGoogleLogin = params.get("error");
    useEffect(() => {
        if (googleError) {
            window.history.replaceState({}, "", window.location.pathname);//replace state with default page after reading once
        }
    })

    async function handleLogin(event) {
        event.preventDefault()

        try {
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

            if(!response.ok) {
                throw new Error('Invalid email or password');
            }

            const data = await response.json();

            localStorage.setItem('token', data.token);

            window.dispatchEvent(new Event("authChange"));

            navigate('/problems') //goes to problems page after login
        }catch (error) {
            setError(error.message);
        }
    }

    return (
        <main className="auth-page">
            <div className="auth-card">
                <h1>Welcome back</h1>
                <p>Enter your credentials to login</p>

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        {error === "user_not_found" && ( //for Google login
                            <p className={"error-message"}>
                                No user found
                            </p>
                        )}
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

                    {error && <p className="login-error">{error}</p>}

                    <button type="submit">Login</button>

                    <button className={"google-button"}
                    onClick={() => {
                        window.location.href =
                            "http://localhost:8080/oauth2/authorization/google?action=login&prompt=select_account";
                    }}
                    >
                        <img src="/googleIcon.svg" alt={"Google"} />
                    </button>

                </form>
            </div>
        </main>
    )
}

export default Login