import React from "react";
import {Route, Routes} from "react-router-dom";
import MainRouter from './MainRouter';
import AuthRouter from "./AuthRouter";

export default function Routers() {
    return (
        <div>
            <Routes>
                <Route path="/*" element={<MainRouter/>}/>
                <Route path="/users/*" element={<AuthRouter/>}/>
            </Routes>
        </div>
    );
}