use yew::prelude::*;
use yew::services::fetch::{FetchService, FetchTask, Request, Response};
use serde::{Deserialize, Serialize};
use wasm_bindgen::prelude::*;

#[derive(Serialize, Deserialize, Debug, Clone)]
struct OwnershipRequest {
    car_price: f64,
    annual_insurance_cost: f64,
    tracker_cost: f64,
    loan_term_years: i32,
    annual_interest_rate: f64,
}

#[derive(Deserialize, Debug, Clone)]
struct OwnershipResponse {
    breakdown: String,
}

enum Msg {
    Submit,
    ReceiveResponse(Result<OwnershipResponse, anyhow::Error>),
    UpdateCarPrice(String),
    UpdateAnnualInsuranceCost(String),
    UpdateTrackerCost(String),
    UpdateLoanTermYears(String),
    UpdateAnnualInterestRate(String),
}

struct Model {
    link: ComponentLink<Self>,
    fetch_task: Option<FetchTask>,
    car_price: String,
    annual_insurance_cost: String,
    tracker_cost: String,
    loan_term_years: String,
    annual_interest_rate: String,
    result: Option<String>,
}

impl Component for Model {
    type Message = Msg;
    type Properties = ();

    fn create(_: Self::Properties, link: ComponentLink<Self>) -> Self {
        Self {
            link,
            fetch_task: None,
            car_price: String::new(),
            annual_insurance_cost: String::new(),
            tracker_cost: String::new(),
            loan_term_years: String::new(),
            annual_interest_rate: String::new(),
            result: None,
        }
    }

    fn update(&mut self, msg: Self::Message) -> ShouldRender {
        match msg {
            Msg::Submit => {
                let request = OwnershipRequest {
                    car_price: self.car_price.parse().unwrap_or(0.0),
                    annual_insurance_cost: self.annual_insurance_cost.parse().unwrap_or(0.0),
                    tracker_cost: self.tracker_cost.parse().unwrap_or(0.0),
                    loan_term_years: self.loan_term_years.parse().unwrap_or(0),
                    annual_interest_rate: self.annual_interest_rate.parse().unwrap_or(0.0),
                };

                let request = Request::post("/api/car-ownership/calculate")
                    .header("Content-Type", "application/json")
                    .body(Json(&request))
                    .expect("Failed to build request.");

                let callback = self.link.callback(
                    |response: Response<Json<Result<OwnershipResponse, anyhow::Error>>>| {
                        let Json(data) = response.into_body();
                        Msg::ReceiveResponse(data)
                    },
                );

                let task = FetchService::fetch(request, callback).expect("Failed to start request");
                self.fetch_task = Some(task);
            }
            Msg::ReceiveResponse(response) => {
                self.result = response.map(|res| res.breakdown).ok();
                self.fetch_task = None;
            }
            Msg::UpdateCarPrice(value) => self.car_price = value,
            Msg::UpdateAnnualInsuranceCost(value) => self.annual_insurance_cost = value,
            Msg::UpdateTrackerCost(value) => self.tracker_cost = value,
            Msg::UpdateLoanTermYears(value) => self.loan_term_years = value,
            Msg::UpdateAnnualInterestRate(value) => self.annual_interest_rate = value,
        }
        true
    }

    fn view(&self) -> Html {
        html! {
            <div>
                <h1>{ "Car Ownership Calculator" }</h1>
                <input
                    type="text"
                    placeholder="Car Price"
                    value=&self.car_price
                    oninput=self.link.callback(|e: InputData| Msg::UpdateCarPrice(e.value))
                />
                <input
                    type="text"
                    placeholder="Annual Insurance Cost"
                    value=&self.annual_insurance_cost
                    oninput=self.link.callback(|e: InputData| Msg::UpdateAnnualInsuranceCost(e.value))
                />
                <input
                    type="text"
                    placeholder="Tracker Cost"
                    value=&self.tracker_cost
                    oninput=self.link.callback(|e: InputData| Msg::UpdateTrackerCost(e.value))
                />
                <input
                    type="text"
                    placeholder="Loan Term Years"
                    value=&self.loan_term_years
                    oninput=self.link.callback(|e: InputData| Msg::UpdateLoanTermYears(e.value))
                />
                <input
                    type="text"
                    placeholder="Annual Interest Rate"
                    value=&self.annual_interest_rate
                    oninput=self.link.callback(|e: InputData| Msg::UpdateAnnualInterestRate(e.value))
                />
                <button onclick=self.link.callback(|_| Msg::Submit)>{ "Calculate" }</button>
                <div>
                    { self.result.as_ref().map(|res| html! { <p>{ res }</p> }).unwrap_or_default() }
                </div>
            </div>
        }
    }
}

#[wasm_bindgen(start)]
pub fn run_app() {
    App::<Model>::new().mount_to_body();
} 