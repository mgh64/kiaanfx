
db.banks.aggregate([
	{$unwind: "$branches"},
	{$group: {_id : {id : '$_id',name:'$name'}, total:{$sum :1}}},
	{$project : {_id : '$_id.id', name : '$_id.name', count : '$total'}},
	{$sort: {_id : 1}}
]); 
