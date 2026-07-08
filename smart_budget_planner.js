let planText = "";

function generatePlan() {
  const budget = Number(document.getElementById("budget").value);
  const place = document.getElementById("place").value;
  const priority = document.getElementById("priority").value;

  if (!budget || !place || !priority) {
    alert("Please fill all fields");
    return;
  }

  // Base split
  let food = budget * 0.25;
  let shopping = budget * 0.15;
  let stay = budget * 0.35;
  let travel = budget * 0.25;

  // Boost priority area
  const boost = budget * 0.10;

  if (priority === "food") food += boost;
  if (priority === "shopping") shopping += boost;
  if (priority === "stay") stay += boost;
  if (priority === "travel") travel += boost;

  // Normalize total
  const totalUsed = food + shopping + stay + travel;
  const factor = budget / totalUsed;

  food = Math.round(food * factor);
  shopping = Math.round(shopping * factor);
  stay = Math.round(stay * factor);
  travel = Math.round(travel * factor);

  planText =
  "Smart Trip Budget Plan\n\n" +
  "Place: " + place + "\n" +
  "Total Budget: ₹" + budget + "\n\n" +
  "Food: ₹" + food + "\n" +
  "Shopping: ₹" + shopping + "\n" +
  "Stay: ₹" + stay + "\n" +
  "Local Travel: ₹" + travel + "\n\n" +
  "Priority Spending: " + priority + "\n\n" +
  "Suggested for " + place + ":\n" +
  "- Popular food spots\n" +
  "- Local attractions\n" +
  "- Budget-friendly travel routes";


  document.getElementById("output").innerHTML = `
    <b>Place:</b> ${place}<br>
    <b>Total Budget:</b> ₹${budget}<br>
    <b>Priority:</b> ${priority}<br><br>

    🍽 Food: ₹${food}<br>
    🛍 Shopping: ₹${shopping}<br>
    🏨 Stay: ₹${stay}<br>
    🚕 Local Travel: ₹${travel}<br>
  `;
}

function downloadPDF() {
  const budget = document.getElementById("budget").value;
  const place = document.getElementById("place").value;
  const priority = document.getElementById("priority").value;

  if (!budget || !place || !priority) {
    alert("Generate the budget plan first");
    return;
  }

  const { jsPDF } = window.jspdf;
  const pdf = new jsPDF();

  let y = 15;

  pdf.text("Smart Trip Budget Plan", 10, y); y += 10;
  pdf.text("----------------------------", 10, y); y += 10;

  pdf.text("Place: " + place, 10, y); y += 8;
  pdf.text("Total Budget: Rs. " + budget, 10, y); y += 12;

  pdf.text("Budget Allocation:", 10, y); y += 8;
  pdf.text("Food: Rs. " + food, 10, y); y += 8;
  pdf.text("Shopping: Rs. " + shopping, 10, y); y += 8;
  pdf.text("Stay: Rs. " + stay, 10, y); y += 8;
  pdf.text("Local Travel: Rs. " + travel, 10, y); y += 12;

  pdf.text("Priority Spending Area: " + priority, 10, y); y += 12;

  pdf.text("Suggested for " + place + ":", 10, y); y += 8;
  pdf.text("- Popular food spots", 10, y); y += 8;
  pdf.text("- Local attractions", 10, y); y += 8;
  pdf.text("- Budget-friendly travel routes", 10, y);

  pdf.save("Smart_Trip_Budget_Plan.pdf");
}


