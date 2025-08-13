import { produce } from "immer"

export type State = {
    selectedFiles: string[]
}

export type Action =
    | { type: "toggle-selected-file", filename: string }


export const initialState: State = {
    selectedFiles: [],
}

export function reducer(state: State, action: Action): State {
    switch (action.type) {
        case "toggle-selected-file":
            return produce(state, draft => {
                if (draft.selectedFiles.includes(action.filename)) {
                    draft.selectedFiles = draft.selectedFiles.filter(f => f != action.filename)
                } else {
                    draft.selectedFiles = [...draft.selectedFiles, action.filename]
                }
            })
    }
}