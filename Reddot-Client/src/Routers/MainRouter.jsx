import "react";
import { Route, Routes } from "react-router-dom";
import Home from "../Components/Main/Home";
import Question from "../Components/Main/Questions";
import About from "../Components/Main/About";

const MainRouter = () => {
  return (
    <div>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/questions" element={<Question />} />
      </Routes>
    </div>
  );
};

export default MainRouter;
