import "react";
import { Route, Routes } from "react-router-dom";
import MainRouter from "./MainRouter";

export default function Routers() {
  return (
    <div>
      <Routes>
        <Route path="/*" element={<MainRouter />}></Route>
      </Routes>
    </div>
  );
}
