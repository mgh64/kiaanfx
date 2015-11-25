db.banks.aggregate([
    { $unwind: '$branches'},    
    {
	$project: {       
	    	branch_id: '$branches.branch_id',	    	     	    
	    	name: '$name',
	    	branch_name: '$branches.name',
	    	city: '$branches.city',
        }
    },
    { $sort : { "name" : 1, "branch_id" : 1 } }
])
