var b = {};
var c = [];
db.buy.aggregate([
    {"$unwind": "$items"},    
    {"$group": 
	{
	    _id: {_id: "$_id", num: "$num", person_id: "$person_id", discount: "$discount", increase: "$increase"},
	    total: {$sum: {$multiply:["$items.value","$items.price"]}}	
	}      
    },
    {"$project" : 
	{
	    _id : '$_id._id',	    
	    person_id : "$_id.person_id",
	    num : "$_id.num",
	    //discount : "$_id.discount",
	    //increase : "$_id.increase",
	    sum: {$add: ["$total", "$_id.discount", "$_id.increase"]}	    
	}	
    },
   {
      "$sort":
      {
         "_id":1
      }
   }
]).forEach(
    function(_n){	
	//_n.personId = db.persons.findOne({"items._id": _n.person_id}).items[0].personId;
	var temp = db.persons.findOne({"items._id": _n.person_id} ,{"items.personId" : 1, "items.first_name" : 1, "items.last_name" : 1}).items[0] ;
	_n.personId = temp.personId ;
	_n.first_name = temp.first_name ;
	_n.last_name = temp.last_name ;		
	_n.date = _n._id.getTimestamp();	
	c.push(_n);
	//db.n.insert(_n)
    }
);
c;
/*db.buy.aggregate([
    {$unwind: "$items"},    
    {$group: 
      {
	_id: "$_id",
	total: {$sum: {$multiply:["$items.value","$items.price"]}}
	
      }      
    }
]);  */
