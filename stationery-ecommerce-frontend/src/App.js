import React from 'react';
import AppRoutes from "./routes/AppRoutes";
import {BrowserRouter} from "react-router-dom";
import {Toaster} from "react-hot-toast";
import AppToaster from "./components/AppToaster";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <AppRoutes/>
                <AppToaster/>
            </div>
        </BrowserRouter>
    );
}

export default App;