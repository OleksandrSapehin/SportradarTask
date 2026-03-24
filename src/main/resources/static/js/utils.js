export function fillSelect(id, data, valueField, textField) {
    const select = document.getElementById(id);
    if (!select) return;
    select.innerHTML = "<option value=''>-- select --</option>";
    data.forEach(item => {
        const option = document.createElement("option");
        option.value = item[valueField];
        option.textContent = item[textField];
        select.appendChild(option);
    });
}

export function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) modal.style.display = "none";
}

export function setMinDateTime() {
    const today = new Date().toISOString().slice(0, 16);
    const dateTimeFields = ['dateTime', 'editDateTime'];
    dateTimeFields.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.min = today;
    });
}