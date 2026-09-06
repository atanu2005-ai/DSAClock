import {Link} from "react-router-dom";

function Home() {
    return (
        <main className={"hero"}>
            <h1>DSAClock</h1>
            <p>
                Track your DSA journey
                <br/>
                Practice. Track. Improve.
            </p>

            <Link to={"/register"} className={"hero-button"}>
                Get started
            </Link>
        </main>
    )
}

export default Home