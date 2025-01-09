export const validateTitle = (title, existing) => {
	if (!title.trim()) {
		return "Title is required";
	}

	const isDuplicate = existing.some((group) => group.title === title);
	if (isDuplicate) {
		return "Title already in use";
	}

	return "";
};
export const validateLabel = (label, existing) => {
	if (!label.trim()) {
		return "Label is required";
	}

	const isDuplicate = existing.some((group) => group.label === label);
	if (isDuplicate) {
		return "Label already in use";
	}

	return "";
};

export const validateIcon = (icon) => {
	if (!icon.trim()) {
		return "Icon is required";
	}

	return "";
};

export const validateColor = (color) => {
	if (!color.trim() || color === "#ffffff") {
		return "Color is required";
	}

	return "";
};

export const validateDescription = (description, existing) => {
	if (!description.trim()) {
		return "Description is required";
	}

	if (description === "<p><br></p>") {
		return "Description is required";
	}

	const isDuplicate = existing.some(
		(group) => group.description === description
	);
	if (isDuplicate) {
		return "Description already in use";
	}

	return "";
};

export const validateContentDiscussion = (content, existing) => {
	if (!content.trim()) {
		return "Content is required";
	}

	if (content === "<p><br></p>") {
		return "Content is required";
	}

	const isDuplicate = existing.some((group) =>
		group.comments?.some((comment) => comment.content === content)
	);
	if (isDuplicate) {
		return "Content already in use";
	}

	return "";
};

export const validateContent = (content, existing) => {
	if (!content.trim()) {
		return "Content is required";
	}

	if (content === "<p><br></p>") {
		return "Content is required";
	}

	const isDuplicate = existing.some((group) => group.content === content);
	if (isDuplicate) {
		return "Content already in use";
	}

	return "";
};

export const validateKeyword = (keyword, existing) => {
	if (!keyword.trim()) {
		return "Keyword is required";
	}

	const isDuplicate = existing.some((a) => a.keyword === keyword);
	if (isDuplicate) {
		return "Keyword already in use";
	}
};

export const replaceBannedWords = (text, bannedWords) => {
	let modifiedText = text;
	bannedWords.forEach((banned) => {
		const regex = new RegExp(banned.keyword, "gi");
		const replacement = "*".repeat(banned.keyword.length);
		modifiedText = modifiedText.replace(regex, replacement);
	});
	return modifiedText;
};
