import { Spinner } from "flowbite-react"
import { useEffect, useReducer, useRef } from "react"
import { getLocalIP, listFiles } from "./lib/api"
import { useAPI } from "./lib/hooks/useAPI"
import { FiUploadCloud, FiDownload } from "react-icons/fi";
import { FileCard } from "./components/FileCard";
import { initialState, reducer } from "./lib/reducer/homeReducer";

export function App() {
  const [state, dispatch] = useReducer(reducer, initialState)
  const uploadFileRef = useRef<HTMLInputElement>(null)
  const localIPRequest = useAPI(getLocalIP)
  const listFilesRequest = useAPI(listFiles)

  useEffect(() => {
    localIPRequest.call(null)
    listFilesRequest.call(null)
  }, [])

  return (
    <div className="container mx-auto p-6 h-screen flex flex-col">
      <div className="flex justify-between mb-6">
        <div className="flex space-x-3">
          <h1 className="text-2xl font-bold my-auto">Warp</h1>

          {localIPRequest.request.loading && (
            <span className="my-auto">
              <Spinner size="sm" />
            </span>
          )}

          {(!localIPRequest.request.loading && localIPRequest.request.state === "success") && (
            <span className="bg-blue-50 text-blue-700 text-sm rounded-full py-1 px-3 my-auto">{localIPRequest.request.data.ip}</span>
          )}
        </div>

        <div className="flex space-x-2">
          <button title="Download" className="flex space-x-2 hover:bg-slate-50 hover:text-blue-700 rounded px-3 py-2 cursor-pointer disabled:cursor-not-allowed transition-colors disabled:opacity-50" disabled={state.selectedFiles.length === 0}>
            <FiDownload size={20} color="gray" className="my-auto" />
            <span className="text-sm text-slate-800 my-auto">Download</span>
          </button>

          <button title="upload" className="flex cursor-pointer space-x-2 hover:bg-slate-50 hover:text-blue-700 rounded px-3 py-2 transition-colors" onClick={() => uploadFileRef.current?.click()}>
            <FiUploadCloud size={20} color="gray" className="my-auto" />
            <span className="text-sm text-slate-800 my-auto">Upload</span>
            <input type="file" className="hidden" name="file" ref={uploadFileRef} />
          </button>
        </div>
      </div>

      <div className="flex-1 bg-gray-50 rounded overflow-y-auto">
        {listFilesRequest.request.loading && (
          <span className="m-auto">
            <Spinner size="md" />
          </span>
        )}


        {!listFilesRequest.request.loading && listFilesRequest.request.state == "success" && (
          <div className="grid grid-cols-6 gap-6 p-6 w-full">
            {listFilesRequest.request.data.files.map(file => (
              <FileCard onClick={() => dispatch({ type: "toggle-selected-file", filename: file })}
                key={file}
                filename={file}
                selected={state.selectedFiles.includes(file)}
              />
            ))}
          </div>
        )}

      </div>
    </div>
  )
}
