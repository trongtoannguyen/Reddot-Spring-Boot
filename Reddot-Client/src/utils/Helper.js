export function getUrlParameter(name, location) {
  name = name.replace('[', '\\[').replace(']', '\\]');
  const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

  let results = regex.exec(location.search);
  return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// set param url
export function pathParams(data) {
  let params = new URLSearchParams();
  Object.keys(data).forEach(key => {
    data[key] && params.append(key, data[key]);
  })
  return params;
}

//convert name role;
function convertNameRole(role) {
  let name = "";
  switch (role) {
    case "ROLE_ADMIN":
      name = "Admin";
      break;
    case "ROLE_USER":
      name = "User";
      break;
    case "ROLE_MODERATOR":
      name = "Moderator";
      break;
    default:
      break;
  }
  return name;
}

//convert list name role
export function convertListNameRole(roles) {
  let names = '';
  roles.forEach(role => {
    names += convertNameRole(role) + ", ";
  });
  names = names.slice(0, -2);
  return names;
}