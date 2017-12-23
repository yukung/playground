require_relative 'boot'

require 'rails/all'

# Require the gems listed in Gemfile, including any gems
# you've limited to :test, :development, or :production.
Bundler.require(*Rails.groups)

module SampleApp
  class Application < Rails::Application
    # Settings in config/environments/* take precedence over those specified here.
    # Application configuration should go into files in config/initializers
    # -- all .rb files in that directory are automatically loaded.

    # To Avoid the "uninitialized constant #{model}::#{uploader_class_name} caused by Carrierwave.
    # See: https://github.com/carrierwaveuploader/carrierwave/issues/399#issuecomment-51422423
    config.autoload_paths += Dir[Rails.root.join('app', 'uploaders')]

    # 認証トークンを remote フォームに埋め込む
    config.action_view.embed_authenticity_token_in_remote_forms = true
  end
end
