db.persons.findOne(
    {"items.personId": 1},
    {"items.$": 1, "_id": 0}
).items[0]

لیست فاکتورها:

db.buy.aggregate([
    {$unwind: "$items"},
    {$group: {
	_id: {	_id: "$_id", num: "$num", date: "$date", 
		person_id: "$person_id", discount: "$discount", increase: "$increase"
		},
	total: {$sum: {$multiply:["$items.value","$items.price"]}}
	}
    },
    {$project: 
	{
	    _id: "$_id._id",
	    num: "$_id.num",
	    date: "$_id.date",
	    person_id: "$_id.person_id",
	    discount: "$_id.discount",
	    increase: "$_id.increase",
	    sum: {$add: ["$total", "$_id.discount", "$_id.increase"]}
	}
    }
]).pretty();


ریز فاکتور:
db.buy_invoices.aggregate([{$unwind: "$items"},{$project: {_id: 1, total: {$multiply:
["$items.value","$items.price"]}}}]);


db.buy.aggregate([
    {$unwind: "$items"},
    {$group: {
	_id: {"_id":"$_id", "number": "$number", "date": "$date", "discount": "$discount"},
	total: {$sum: {$multiply:["$items.value","$items.price"]}}
	}};
    {$project: {
	"_id":0, 
	"date":"$_id.date", 
	"total": {$add: ["$total","$_id.discount"]},
	"number":"$_id.number"}
	}
]);

