import axios from "axios";
import { API_BASE_URL } from "../constants";

const BASE_URL = `${API_BASE_URL}/api/v1`;

const instance = axios.create({
	baseURL: BASE_URL,
});

instance.interceptors.response.use(
	function (response) {
		return response.data ? response.data : { statusCode: response.status };
	},
	function (error) {
		let res = {};
		if (error.response) {
			res.data = error.response.data;
			res.status = error.response.status;
			res.headers = error.response.headers;
		} else if (error.request) {
			console.log(`error request>>`, error.request);
		} else {
			console.log(`error message>>`, error.message);
		}
		return res;
	}
);

export default instance;
