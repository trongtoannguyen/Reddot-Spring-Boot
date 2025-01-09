// Helper function to pad single-digit numbers with a leading zero
function padZero(number) {
	return number.toString().padStart(2, "0");
}


// Path: src/utils/formatDate.js

export function formatDate(dateString) {
	const date = new Date(dateString);
	const day = padZero(date.getDate());
	const month = padZero(date.getMonth() + 1);
	const year = date.getFullYear();
	return `${day}/${month}/${year}`;
}

export function formatDifferentUpToNow(dateString) {
	const date = new Date(dateString);
	const diff = Math.abs(Date.now() - date);
	const days = Math.floor(diff / (1000 * 60 * 60 * 24));
	const hours = Math.floor((diff / (1000 * 60 * 60)) % 24);
	const minutes = Math.floor((diff / 1000 / 60) % 60);
	if (days > 0) {
		return `${days} days ago`;
	} else if (hours > 0) {
		return `${hours} hours ago`;
	} else {
		return `${minutes} minutes ago`;
	}
}


export function formatLongDate(dateString){
	const options = { year: "numeric", month: "long", day: "numeric" };
	return new Intl.DateTimeFormat("en-US", options).format(
		new Date(dateString)
	);
}


