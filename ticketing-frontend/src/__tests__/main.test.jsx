import { render, screen } from '@testing-library/react'
import { createRoot } from 'react-dom/client'
import App from '../App'
import '../main'

// Mock react-dom/client
jest.mock('react-dom/client', () => ({
  createRoot: jest.fn(() => ({
    render: jest.fn()
  }))
}))

describe('Main Entry Point', () => {
  beforeEach(() => {
    // Clear DOM between tests
    document.body.innerHTML = ''
    // Reset mocks
    jest.clearAllMocks()
    // Setup root div
    const root = document.createElement('div')
    root.id = 'root'
    document.body.appendChild(root)
  })

  test('root element exists in document', () => {
    const rootElement = document.getElementById('root')
    expect(rootElement).toBeTruthy()
  })

  test('createRoot is called with root element', () => {
    const rootElement = document.getElementById('root')
    require('../main')
    expect(createRoot).toHaveBeenCalledWith(rootElement)
  })

  test('render is called with App wrapped in StrictMode', () => {
    const renderMock = jest.fn()
    createRoot.mockImplementation(() => ({
      render: renderMock
    }))
    
    require('../main')
    
    expect(renderMock).toHaveBeenCalled()
    expect(renderMock.mock.calls[0][0].type.name).toBe('StrictMode')
    expect(renderMock.mock.calls[0][0].props.children.type).toBe(App)
  })
})
