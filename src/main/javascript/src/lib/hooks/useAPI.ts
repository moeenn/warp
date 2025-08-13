import { useState } from "react"

type APIRequestState<T> =
    | { loading: true }
    | { loading: false; state: "idle" }
    | { loading: false; state: "success"; data: T }
    | { loading: false; state: "error"; error: Error }

export function useAPI<T, E>(fetcher: (params: T) => Promise<E>) {
    const [state, setState] = useState<APIRequestState<E>>({
        loading: false,
        state: "idle",
    })
    const call = (callParams: T) => {
        setState({ loading: true })
        return new Promise<E>((resolve, reject) => {
            fetcher(callParams)
                .then((res) => {
                    setState({ loading: false, state: "success", data: res })
                    resolve(res)
                })
                .catch((err) => {
                    setState({ loading: false, state: "error", error: err })
                    reject(err)
                })
        })
    }

    return {
        request: state,
        call,
    }
}