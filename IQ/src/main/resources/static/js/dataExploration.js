var constraint_for_exploration;
function getSC(){
    var pos = 0;
    if (constraint_for_exploration){
        for (var j = 0; j < constraint_for_exploration.length; j++) {
            var val = constraint_for_exploration[j];
            id = val.id
            position = val.position
            var cst = document.getElementById(id);
            if (cst.checked){
                pos += Math.pow(2, position)
                //alert(id)
            }
        }

    }
    //alert(pos)
    return pos;
}

function loadConstraints_for_exploration(){
    fetch(adr+"getConstraints").then(e => e.json()).then(e => {
		constraint_for_exploration = e;
		var constraints_loaded = document.getElementById("constraints_loaded");
		var res = '';
		for (var j = 0; j < e.length; j++) {
            var val = e[j];
            res += '<div style="display: inline-block; margin-right: 10px;"><input type="checkbox" id="'+val.id+'" value="'+val.id+'"></input><label for="'+val.id+'" class="commun_text">'+val.id+'</label></div>';
		}
		constraints_loaded.innerHTML = res;
	}).catch(e => alert(e));
}


function getTuplesProportion(vc){
    var data = "{'constraints':"+vc+"}";
    var param = {'method':'Post', 'body':data};
    var url = adr+"exploration/tuple/proportion/"
    fetch(url, param).then(e => e.json()).then(e => {
        display_tuple_proprtions_vio(e.X, e.Y);
        display_tuple_number_vio(e.X, e.Y2);
    }).catch(e => alert(e));
}

function changeConstraints(){
    //alert('hello')
    var vc = getSC();
    //alert(vc)
    if (vc != 0){
        getTuplesProportion(vc);
        //loadViolations(vc);
    }
}

function start_data_exploration(){
    //load Constraints
    loadConstraints_for_exploration();
    //changeConstraints();
}

function tuple_proprtions_vio_load(){
    var y = document.getElementById("tuple_proprtions_vio_");
    y.innerHTML = '<canvas id="tuple_proprtions_vio"></canvas>'
}

function tuple_proprtions_vio_remove(){
    var y = document.getElementById("tuple_proprtions_vio_");
    y.innerHTML = ''
}

function display_tuple_number_vio(X, Y){   
    var y = document.getElementById("tuple_number_vio_");
    y.innerHTML = '<canvas id="tuple_number_vio"></canvas>';
    var canvas = document.getElementById('tuple_number_vio')

    var ctx3 = canvas.getContext('2d');
    var chart3 = new Chart(ctx3, {
        type: 'bar',
        data: {
            labels: X,
            datasets: [{
                label: 'Number of Violations',
                data: Y,
                backgroundColor: 'green'
            }]
        },
        options: {
            legend: {
                    display: false,
                    labels: {
                    display: false,
                    fontStyle: "bold",
                    fontColor: "black",
                    fontSize: size__
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontStyle: "bold",
                        fontColor: "black",
                        fontSize: size__
                    },
                    type: 'logarithmic'
                }],
                xAxes: [{
                    ticks: {
                        display: false,
                        fontStyle: "bold",
                        fontColor: "black",
                        fontSize: size__
                    }
                }]
            }
    }
    });	    
}

function display_tuple_proprtions_vio(X, Y){   
    tuple_proprtions_vio_remove();
    tuple_proprtions_vio_load();
    var canvas = document.getElementById('tuple_proprtions_vio')
    //canvas.height = '100'; 
    //canvas.width = "500";   
    var ctx3 = canvas.getContext('2d');
    var chart3 = new Chart(ctx3, {
        type: 'bar',
        data: {
            labels: X,
            datasets: [{
                label: 'Proportion of Violations',
                data: Y,
                backgroundColor: 'blue'
            }]
        },
        options: {
            legend: {
                display: false,
                labels: {
                    display: false,
                    fontStyle: "bold",
                    fontColor: "black",
                    fontSize: size__
                }
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        fontStyle: "bold",
                        fontColor: "black",
                        fontSize: size__
                    },
                    type: 'logarithmic'
                }],
                xAxes: [{
                    ticks: {
                        display: false,
                        fontColor: "black",
                        fontStyle: "bold",
                        fontSize: size__
                    }
                }]
            }
    }
    });	    
}


function showTups(a, b){
    alert(a+'::'+b)
}