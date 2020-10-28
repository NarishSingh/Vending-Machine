let DataService = function () {
    let self = this;

    /**
     * Attempt to retrieve all items from server via GET
     * @param callback {function} to refresh and render table on page
     * @param errorFunc {function} to render error message on page
     */
    self.getAllItems = function (callback, errorFunc) {
        $.ajax({
            type: 'GET',
            url: 'http://tsg-vending.herokuapp.com/items',
            success: callback,
            error: errorFunc
        });
    };

    /**
     * Attempt to vend a purchased item from server via POST
     * @param moneyInput {string} a cash amount, formatted to 2 precision
     * @param itemId {number} an item's id from server
     * @param callback {function} to vend an purchase item and refresh table on page
     * @param errorFunc {function} to render error message on page
     */
    self.vendItem = function (moneyInput, itemId, callback, errorFunc) {
        $.ajax({
            type: 'POST',
            url: 'http://tsg-vending.herokuapp.com/money/' + moneyInput + '/item/' + itemId,
            success: callback,
            error: errorFunc
        });
    }
}
