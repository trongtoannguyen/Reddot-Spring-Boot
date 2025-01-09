import { USER_REGEX, EMAIL_REGEX, PWD_REGEX } from "../constants";

// Extracted validation logic
export const validateName = (username) => USER_REGEX.test(username);
export const validateEmail = (email) => EMAIL_REGEX.test(email);
export const validatePassword = (password) => PWD_REGEX.test(password);
export const validateConfirm = (password, confirm) => password === confirm;
export const validateNameOrEmail = (value) => (validateName(value) || validateEmail(value));

export const errorPasswordItem = (password) => {
  if (validatePassword(password)) {
    return null;
  }
  let errorMsg = [];
  if (password.length < 8 || password.length > 24) {
    errorMsg.push("Password must be 8-24 characters long");
  }
  //And contain at least one lowercase letter, one uppercase letter, one number.
  const PWD_1_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{3,}$/;
  if (!PWD_1_REGEX.test(password)) {
    errorMsg.push("Password must contain at least one lowercase letter, one uppercase letter, one number");
  }
  //And contain at least one special character(@#$%)
  const PWD_2_REGEX = /^(?=.*[@#$%!]).+$/;
  if (!PWD_2_REGEX.test(password)) {
    errorMsg.push("Password must contain at least one special character(@#$%!)");
  }
  return errorMsg;
}


// check string isNul
const isNull = (str) => {
  return str === null || str === undefined || str === "";
}

export const validateString = (str) => {
  return !isNull(str);
}

export const validateNumber = (num) => {
  return !isNaN(num) && num >= 0;
}