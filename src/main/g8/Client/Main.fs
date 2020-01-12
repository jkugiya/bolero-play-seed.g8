module $name;format="Camel"$.Client.Main

open System
open Elmish
open Bolero
open Bolero.Html
open Bolero.Json
open Bolero.Remoting
open Bolero.Remoting.Client
open Bolero.Templating.Client


/// Routing endpoints definition.
type Page =
    | [<EndPoint "/">] Home

/// The Elmish application's model.
type Model =
    {
        page: Page
        error: string option
        active: bool
    }

let initModel =
    {
        page = Home
        error = None
        active = true
    }

type Message =
    | Error of exn
    | ClearError
    | SetActive
    | SetPage of Page

let update message model =
    match message with
    | SetPage page ->
        { model with page = page}, Cmd.none
    | SetActive ->
        { model with active = not model.active }, Cmd.none
    | Error exn ->
        { model with error = Some exn.Message }, Cmd.none
    | ClearError ->
        { model with error = None }, Cmd.none

/// Connects the routing system to the Elmish application.
let router = Router.infer SetPage (fun model -> model.page)

let view model dispatch =
    div [] [text "Hello World"]

type MyApp() =
    inherit ProgramComponent<Model, Message>()
    override this.Program =
        Program.mkProgram (fun _ -> initModel, Cmd.ofMsg (SetPage Home)) update view
        |> Program.withRouter router
#if DEBUG
        |> Program.withHotReload
#endif
