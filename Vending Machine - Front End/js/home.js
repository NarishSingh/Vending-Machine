/*NAMESPACE*/
let ds = new DataService();
let money = 0.00;
let itemCt = 1;
//purchase specific vars
let itemSelected = null;
let itemPrice = null;

/*MAIN*/
$(document).ready(function () {
    ds.getAllItems(refreshItems, updateMsg);

    /*MONEY CLICK EVENT HANDLERS*/
    $(document).on('click', '#add-dollar-btn', onAddDollarClicked);
    $(document).on('click', '#add-quarter-btn', onAddQuarterClicked);
    $(document).on('click', '#add-dime-btn', onAddDimeClicked);
    $(document).on('click', '#add-nickel-btn', onAddNickelClicked);
    $(document).on('click', '#add-penny-btn', onAddPennyClicked);

    /*PURCHASE EVENT HANDLERS*/
    $(document).on('click', '.item-box', onItemBoxClicked);
    $(document).on('click', '#purchase-btn', onPurchaseItemClicked);
    $(document).on('click', '#change-return-btn', onChangeReturnClicked);
});

/*METHODS*/

/*Rendering*/
/**
 * Format an item from db into HTML for rendering
 * @param item {*} contains id, name, price, quantity
 */
function formatItemBox(item) {
    item.currentItemCount = itemCt; //save item count to item

    //format price
    let itemPrice = parseFloat(item.price);
    let itemPriceString = "$" + itemPrice.toFixed(2);

    //div
    let itemDiv =
        `<div class="item-box" 
            data-itemct='${item.currentItemCount}' data-itemname='${item.name}' data-itemid='${item.id}' 
            data-itemprice='${item.price}'>
        <p>` + itemCt + `</p>
        <p class="font-weight-bold">${item.name}</p>
        <p>` + itemPriceString + `</p>
        <p>Quantity: ${item.quantity}</p>
    </div>`;

    itemCt++;

    return itemDiv;
}

/**
 * Render all items to page
 * @param itemList {Array} array from the API response
 */
function refreshItems(itemList) {
    itemCt = 1;
    $('#vm-item-grid').empty();

    for (let item of itemList) {
        $('#vm-item-grid').append(formatItemBox(item));
    }
}

/**
 * Add money to machine pre-purchase
 * @param money {number} dollars and cents
 */
function updateMoney(money) {
    let moneyString = '$' + money.toFixed(2);

    $('#money-in').val(moneyString);
}

/**
 * Render transaction or error messages to user
 * @param msg {string} messages concerning items, money added, purchase success, or any error throws from server
 */
function updateMsg(msg) {
    $('#purchase-feedback').val(msg);
}

/**
 * Render the temp item count for UI
 * @param itemDescrp {string} temp item count from clicked on item
 */
function updateItemSelected(itemDescrp) {
    $('#item-to-buy').val(itemDescrp);
}

/**
 * Render coin return to user
 * @param change {string} containing all change to be returned to user post-purchase
 */
function updateChange(change) {
    $('#change-coins').val(change);
}

/**
 * Turn coins into a string
 * @param numCoin {number} number of coins of that type
 * @param coinString {string} type of coin
 * @returns {string} a string representation of the change
 */
function stringifyChange(numCoin, coinString) {
    let changeString = "";

    if (numCoin > 0) {
        if (numCoin === 1) {
            changeString += numCoin + " " + coinString;
        } else {
            if (coinString === "Penny") {
                changeString += numCoin + " Pennies";
            } else {
                changeString += numCoin + " " + coinString + "s";
            }
        }
    }

    return changeString;
}

/*Handlers*/
/**
 * Event handler - add a dollar to money
 * @param e {event} button click
 */
function onAddDollarClicked(e) {
    updateMoney((money += 1.00));
    updateMsg("Dollar Added");
}

/**
 * Event handler - add a quarter to money
 * @param e {event} button click
 */
function onAddQuarterClicked(e) {
    updateMoney((money += 0.25));
    updateMsg("Quarter Added");
}

/**
 * Event handler - add a dime to money
 * @param e {event} button click
 */
function onAddDimeClicked(e) {
    updateMoney((money += 0.10));
    updateMsg("Dime Added");
}

/**
 * Event handler - add a nickel to money
 * @param e {event} button click
 */
function onAddNickelClicked(e) {
    updateMoney((money += 0.05));
    updateMsg("Nickle Added");
}

/**
 * Event handler - add a penny to money
 * @param e {event} button click
 */
function onAddPennyClicked(e) {
    updateMoney((money += 0.01));
    updateMsg("Penny Added");
}

/**
 * Event handler - select an item by clicking item box
 * @param e {event} button click
 */
function onItemBoxClicked(e) {
    updateMsg("");
    updateChange("");

    let itemCt = $(this).data('itemct');
    let itemName = $(this).data('itemname');
    let itemString = itemCt + " - " + itemName;

    //save info for purchase
    itemSelected = $(this).data('itemid');
    itemPrice = $(this).data('itemprice');

    updateItemSelected(itemString);
}

/**
 * Event handler - attempt purchase
 * @param e {event} button click
 */
function onPurchaseItemClicked(e) {
    if (itemSelected === null) {
        updateMsg("Please make a selection");
    } else {
        let moneyFormatted = money.toFixed(2);

        ds.vendItem(moneyFormatted, itemSelected, function (changeData, status) {
            //show change back
            let q = changeData.quarters;
            let d = changeData.dimes;
            let n = changeData.nickels;
            let p = changeData.pennies;

            let changeString = "";

            if (q > 0) {
                changeString += stringifyChange(q, "Quarter");
            }

            if (d > 0) {
                if (q > 0) {
                    changeString += " ";
                }

                changeString += stringifyChange(d, "Dime");
            }

            if (n > 0) {
                if (q || d > 0) {
                    changeString += " ";
                }

                changeString += stringifyChange(n, "Nickel");
            }

            if (p > 0) {
                if (q || d || n > 0) {
                    changeString += " ";
                }

                changeString += stringifyChange(p, "Penny");
            }

            updateChange(changeString);

            //clear and refresh
            money = 0.00;
            itemSelected = null;
            itemPrice = null;
            updateMoney(money);
            updateMsg("Thank you!!!");
            ds.getAllItems(refreshItems, updateMsg);
        }, function (jqXHR, textStatus, errorThrown) {
            updateMsg(jqXHR.responseJSON.message);
            ds.getAllItems(refreshItems, updateMsg);
        });
    }
}

/**
 * Event handler - clear all feedback on screen and return to initial state
 * @param e {event} button click
 */
function onChangeReturnClicked(e) {
    money = 0.00;
    itemSelected = null;
    itemPrice = null;

    updateMoney(money);
    $('#purchase-feedback').val('');
    $('#item-to-buy').val('');
    $('#change-coins').val('');
}
