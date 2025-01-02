import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Login from "../Components/Auth/Login"

export default function AuthRouter(){
    return (
        <div>
         <Routes>
            <Route path="/login" element={<Login/>}/>
         </Routes>
        </div>
    )
};